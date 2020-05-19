package com.dili.dr.provider;

import com.dili.dr.glossary.EnabledEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangmi
 */
@Component
public class EnabledProvider implements ValueProvider {

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        List<ValuePair<?>> BUFFER = new ArrayList<>();
        BUFFER.addAll(Stream.of(EnabledEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString()))
                .collect(Collectors.toList()));
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object object, Map map, FieldMeta fieldMeta) {
        if (null == object) {
            return null;
        }
        List<ValuePair<?>> BUFFER = new ArrayList<>();
        BUFFER.addAll(Stream.of(EnabledEnum.values())
                .map(e -> new ValuePairImpl<>(e.getName(), e.getCode().toString()))
                .collect(Collectors.toList()));
        ValuePair<?> valuePair = BUFFER.stream().filter(val ->
                object.toString().equals(val.getValue())
        ).findFirst().orElseGet(() -> null);
        if (null != valuePair) {
            return valuePair.getText();
        }
        return "";
//        return value ? EnabledEnum.ENABLED.getName() : EnabledEnum.DISABLED.getName();
    }
}
