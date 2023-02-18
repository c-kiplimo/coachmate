package com.natujenge.thecouch.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.natujenge.thecouch.domain.Constants;
public class NotificationUtil {
    private static final String TEMPLATE_REGEX = "\\$\\w+";

    public static String generateContentFromTemplate(String template, Map<String, Object> replacementValues) {
        if (template == null) {
            return null;
        }

        Set<String> templateParams = getTemplateParams(template);

        System.out.println("replacement: " + templateParams);

        return replaceTemplateParams(template, templateParams, replacementValues);
    }

    public static Set<String> getTemplateParams(String template) {

        Pattern pattern = Pattern.compile(TEMPLATE_REGEX);

        Matcher matcher = pattern.matcher(template);

        // matched texts
        Set<String> wordsToReplace = new HashSet<>();
        while (matcher.find()) {
            wordsToReplace.add(matcher.group());
        }

        return wordsToReplace;
    }

    public static String replaceTemplateParams(String template, Set<String> templateParams, Map<String, Object> replacementValues) {

        for (String templateParam : templateParams) {
            String realTemplateParam = templateParam.replaceFirst("\\$", "");

            String replacementValue = String.valueOf(replacementValues.get(realTemplateParam));

            template = template.replaceAll("\\$" + realTemplateParam, replacementValue);
        }

        return template;
    }


    public static void main(String[] args) {

        String template = Constants.DEFAULT_NEW_CONTRACT_SMS_TEMPLATE;

        System.out.println("Template: " + template);


        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", "Boniface Mutea");
        replacementVariables.put("amountBilled", "CY-3456");
        replacementVariables.put("startDate", "02-15-2022");
        replacementVariables.put("endDate", "02-15-2022");
        replacementVariables.put("coachingTopic", 1500);
        replacementVariables.put("business_name", "CakePlus");

        System.out.println("replacement variables: " + replacementVariables);


        String content = generateContentFromTemplate(template, replacementVariables);

        System.out.println("content: " + content);


    }
}