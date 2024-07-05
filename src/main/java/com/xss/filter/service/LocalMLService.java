package com.xss.filter.service;

import com.xss.filter.util.ConstantUtil;
import com.xss.filter.util.RequestUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/generate_regex_patterns")
public class LocalMLService {

    @PostMapping
    public ResponseEntity<Map<String, List<String>>> generateRegexPatterns(@RequestBody RequestUtil requestData) {
        List<String> sampleTexts = requestData.getSampleTexts();
        double threshold = requestData.getThreshold();

        // Process the sampleTexts to generate regex patterns
        List<String> regexPatterns = new ArrayList<>();
        List<Pattern> compiledPatterns = new ArrayList<>();
        for (String text : sampleTexts) {
            // Dummy logic to simulate processing
            if (text.length() > threshold * 10) {
                String quotedText = Pattern.quote(text);
                regexPatterns.add(quotedText);
                compiledPatterns.add(Pattern.compile(quotedText, Pattern.CASE_INSENSITIVE));
            }
        }

        // Update the ConstantUtil with new patterns
        ConstantUtil.updatePatterns(compiledPatterns);

        Map<String, List<String>> response = new HashMap<>();
        response.put("regex_patterns", regexPatterns);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
