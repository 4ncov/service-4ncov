package com.ncov.module.common.util;

import com.ncov.module.common.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageUtils {

    public static String joinImageUrls(List<String> urls) {
        if (CollectionUtils.isEmpty(urls)) {
            return "";
        }
        return String.join(Constants.DELIMITER_COMMA, urls);
    }

    public static List<String> splitImageUrls(String joinedUrls) {
        if (StringUtils.isEmpty(joinedUrls)) {
            return Collections.emptyList();
        }
        return Arrays.stream(joinedUrls.split(Constants.DELIMITER_COMMA)).collect(Collectors.toList());
    }
}
