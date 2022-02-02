package com.cheetah.cheetahtechnicaltest.services.impl;

import com.cheetah.cheetahtechnicaltest.exceptions.UnexpectedException;
import com.cheetah.cheetahtechnicaltest.models.Pair;
import com.cheetah.cheetahtechnicaltest.models.Recipient;
import com.cheetah.cheetahtechnicaltest.models.Tag;
import com.cheetah.cheetahtechnicaltest.services.TagPairingService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yuvaraj
 */

@Service
public class TagPairingServiceImpl implements TagPairingService {

    private static final int TAG_INTERSECTION_SIZE_MIN = 1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Pair> getPairList(String filePath) throws Exception {
        File file = getFile(filePath, true);
        //Read file and convert to object
        String data = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        Tag tag = readValueTagContent(data);
        //Compute the pair method
        List<Pair> pairList = new ArrayList<>();
        for (int i = 0; i < tag.getRecipients().size(); i++) {
            Recipient firstRecipientIteration = tag.getRecipients().get(i);
            for (int j = 0; j < tag.getRecipients().size(); j++) {
                Recipient secondRecipientIteration = tag.getRecipients().get(j);
                //Tag Check null or empty if not skip loop
                //Check id or name equal with outer loop if yes skip loop
                if (secondRecipientIteration.getId() == firstRecipientIteration.getId() || secondRecipientIteration.getName().equalsIgnoreCase(firstRecipientIteration.getName())) {
                    continue;
                }
                //Get set of tag intersection
                Set<String> result = getIntersectionVal(firstRecipientIteration.getTags(), secondRecipientIteration.getTags());
                //If not empty and size bigger than 1
                if (!result.isEmpty() && result.size() > TAG_INTERSECTION_SIZE_MIN) {
                    Pair pair = new Pair(firstRecipientIteration.getName(), secondRecipientIteration.getName(), new ArrayList<>(result));
                    //To check uniqueness of pair if the pair list already contain
                    if (!pairList.contains(pair)) {
                        pairList.add(pair);
                    }
                }
            }
        }
        return pairList;
    }

    private Set<String> getIntersectionVal(List<String> arg1, List<String> arg2) {
        return arg1.stream()
                .distinct()
                .filter(s -> {
                    for (String secondRecipientTag : arg2) {
                        if (secondRecipientTag.equalsIgnoreCase(s)) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private Tag readValueTagContent(String data) {
        if (null == data || data.isEmpty()) {
            logger.info("Data is empty or null");
            throw new UnexpectedException("File is empty");
        }
        try {
            Tag tag = objectMapper.readValue(data, Tag.class);
            if (null == tag) {
                throw new UnexpectedException("Tag model is null");
            }
            if (null == tag.getRecipients() || tag.getRecipients().isEmpty()) {
                throw new UnexpectedException("Tag recipients is null or empty");
            }
            tag.setRecipients(tag.getRecipients().stream().filter(recipient -> null != recipient.getTags() && !recipient.getTags().isEmpty()).collect(Collectors.toList()));
            return tag;
        } catch (Exception e) {
            logger.error("Something wrong with the file {}", e.getMessage());
            throw new UnexpectedException("Something wrong with the file please check again.");
        }
    }

    @Override
    public File getFile(String filePath, boolean throwErrorIfNotAvailable) throws FileNotFoundException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) && throwErrorIfNotAvailable) {
            logger.error("File not found {}", filePath);
            throw new FileNotFoundException("File not found");
        }
        return path.toFile();
    }
}
