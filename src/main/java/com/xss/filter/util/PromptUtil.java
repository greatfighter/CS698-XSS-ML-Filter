package com.xss.filter.util;

import java.util.List;

public class PromptUtil {

    public static List<String> getPrompts() {
        return List.of(
            "Generate a regex pattern to detect <input> tags in HTML:",
            "Generate a regex pattern to detect <span> tags in HTML:",
            "Generate a regex pattern to detect <div> tags in HTML:",
            "Generate a regex pattern to detect <style> tags in HTML:",
            "Generate a regex pattern to detect onload= expressions in HTML:",
            "Generate a regex pattern to detect anything between <script> tags in HTML:",
            "Generate a regex pattern to detect javascript: expressions in HTML:",
            "Generate a regex pattern to detect lone </script> tags in HTML:",
            "Generate a regex pattern to detect <script> tags with attributes in HTML:",
            "Generate a regex pattern to detect src='...' expressions in HTML:",
            "Generate a regex pattern to detect src=\"...\" expressions in HTML:",
            "Generate a regex pattern to detect eval(...) expressions in HTML:",
            "Generate a regex pattern to detect expression(...) expressions in HTML:",
            "Generate a regex pattern to detect vbscript: expressions in HTML:"
        );
    }
}
