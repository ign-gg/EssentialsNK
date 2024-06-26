package cn.yescallop.essentialsnk;

import cn.nukkit.Server;
import cn.nukkit.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Language {

    private static Map<String, String> lang;
    private static Map<String, String> fallbackLang;

    private Language() {
        //no instance
    }

    static void load(String langName) {
        if (lang != null) {
            return;
        }
        try {
            lang = loadLang(Language.class.getClassLoader().getResourceAsStream("lang/" + langName + ".ini"));
        } catch (NullPointerException e) {
            lang = new HashMap<>();
        }
        fallbackLang = loadLang(Language.class.getClassLoader().getResourceAsStream("lang/eng.ini"));
    }

    public static String translate(String str, Object... params) {
        String baseText = get(str);
        baseText = parseTranslation(baseText != null ? baseText : str);
        for (int i = 0; i < params.length; i++) {
            baseText = baseText.replace("{%" + i + "}", parseTranslation(String.valueOf(params[i])));
        }

        return baseText;
    }

    public static String translate(String str, String... params) {
        String baseText = get(str);
        baseText = parseTranslation(baseText != null ? baseText : str);
        for (int i = 0; i < params.length; i++) {
            baseText = baseText.replace("{%" + i + "}", parseTranslation(params[i]));
        }

        return baseText;
    }

    private static Map<String, String> loadLang(InputStream stream) {
        try {
            String content = Utils.readFile(stream);
            Map<String, String> d = new HashMap<>();
            for (String line : content.split("\n")) {
                if (line.isEmpty() || line.charAt(0) == '#') {
                    continue;
                }
                int splitIndex = line.indexOf('=');
                if (splitIndex == -1) {
                    continue;
                }
                String key = line.substring(0, splitIndex);
                String value = line.substring(splitIndex + 1);

                d.put(key, value);
            }
            return d;
        } catch (IOException e) {
            Server.getInstance().getLogger().logException(e);
            return null;
        }
    }

    private static String internalGet(String id) {
        if (lang.containsKey(id)) {
            return lang.get(id);
        } else if (fallbackLang.containsKey(id)) {
            return fallbackLang.get(id);
        }
        return null;
    }

    private static String get(String id) {
        if (lang.containsKey(id)) {
            return lang.get(id);
        } else if (fallbackLang.containsKey(id)) {
            return fallbackLang.get(id);
        }
        return id;
    }

    private static String parseTranslation(String text) {
        StringBuilder newString = new StringBuilder();
        text = String.valueOf(text);

        StringBuilder replaceString = null;

        int len = text.length();

        for (int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            if (replaceString != null) {
                if (((int) c >= 0x30 && (int) c <= 0x39) // 0-9
                        || ((int) c >= 0x41 && (int) c <= 0x5a) // A-Z
                        || ((int) c >= 0x61 && (int) c <= 0x7a) || // a-z
                        c == '.' || c == '-') {
                    replaceString.append(c);
                } else {
                    String t = internalGet(replaceString.substring(1));
                    if (t != null) {
                        newString.append(t);
                    } else {
                        newString.append(replaceString);
                    }
                    replaceString = null;
                    if (c == '%') {
                        replaceString = new StringBuilder(String.valueOf(c));
                    } else {
                        newString.append(c);
                    }
                }
            } else if (c == '%') {
                replaceString = new StringBuilder(String.valueOf(c));
            } else {
                newString.append(c);
            }
        }

        if (replaceString != null) {
            String t = internalGet(replaceString.substring(1));
            if (t != null) {
                newString.append(t);
            } else {
                newString.append(replaceString);
            }
        }
        return newString.toString();
    }
}

