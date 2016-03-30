package com.serotonin.propertyEditor;

import org.springframework.validation.MessageCodesResolver;

import com.serotonin.util.StringUtils;

public class DefaultMessageCodesResolver implements MessageCodesResolver {
    @SuppressWarnings("unchecked")
    public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class fieldType) {
        if ("typeMismatch".equals(errorCode)) {
            if (fieldType == Double.TYPE)
                return new String[] { "badDecimalFormat" };
            if (fieldType == Integer.TYPE)
                return new String[] { "badIntegerFormat" };
        }
        if (StringUtils.isEmpty(errorCode))
            return new String[0];
        return new String[] { errorCode };
    }

    public String[] resolveMessageCodes(String errorCode, String objectName) {
        return resolveMessageCodes(errorCode, objectName, null, null);
    }
}
