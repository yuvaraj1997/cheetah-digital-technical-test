package com.cheetah.cheetahtechnicaltest.services;

import com.cheetah.cheetahtechnicaltest.models.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Yuvaraj
 */
public interface TagPairingService {

    /**
     * Processes to print common tag
     *
     * @param filePath String
     * @return List<Pair>
     */
    List<Pair> getPairList(String filePath) throws Exception;

    /**
     * Processes to get file
     *
     * @param filePath                 String
     * @param throwErrorIfNotAvailable boolean
     */
    File getFile(String filePath, boolean throwErrorIfNotAvailable) throws FileNotFoundException;
}
