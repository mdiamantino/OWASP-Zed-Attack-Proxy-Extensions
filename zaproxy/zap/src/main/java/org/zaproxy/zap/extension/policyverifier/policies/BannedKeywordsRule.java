package org.zaproxy.zap.extension.policyverifier.policies;

import net.htmlparser.jericho.Source;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class BannedKeywordsRule implements Rule {

    private String name = "EnsureBannedKeywords";
    private final String filename = "BannedKeywords.txt";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(HttpMessage httpMessage, Source source) {
        String requestBody = httpMessage.getRequestBody().toString().toLowerCase();
        Collection<String> bannedWords = getBannedKeywords();

        for (String word : bannedWords) {
            word = word.toLowerCase();
             if (requestBody.contains(word))
                 return false;
        }

        return true;
    }

    private Collection<String> getBannedKeywords() {
        Collection<String> bannedWords = new ArrayList<>();
        try {
            File bannedWordsFile = new File(filename);
            Scanner myReader = new Scanner(bannedWordsFile);
            while (myReader.hasNextLine()) {
                String word = myReader.nextLine();
                if (!word.isEmpty())
                    bannedWords.add(word.trim());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading banned words.");
            e.printStackTrace();
        }

        return bannedWords;
    }
}
