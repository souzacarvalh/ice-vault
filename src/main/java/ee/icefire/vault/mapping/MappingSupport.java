package ee.icefire.vault.mapping;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Felipe Carvalho on 2019-03-26.
 */
public interface MappingSupport {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    default <T extends MappingSupport> T transform(Class<T> type) {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(localDateToStringConverter);
        mapper.addConverter(stringToLocalDateConverter);
        mapper.addConverter(localDateTimeToStringConverter);
        mapper.addConverter(stringToLocalDateTimeConverter);
        return mapper.map(this, type);
    }

    Converter<String, LocalDateTime> stringToLocalDateTimeConverter = new AbstractConverter<String, LocalDateTime>() {
        @Override
        protected LocalDateTime convert(String source) {
            LocalDateTime localDate = LocalDateTime.parse(source, dateTimeFormatter);
            return localDate;
        }
    };

    Converter<LocalDateTime, String> localDateTimeToStringConverter = new AbstractConverter<LocalDateTime, String>() {
        @Override
        protected String convert(LocalDateTime source) {
            return source.format(dateTimeFormatter);
        }
    };

    Converter<String, LocalDate> stringToLocalDateConverter = new AbstractConverter<String, LocalDate>() {
        @Override
        protected LocalDate convert(String source) {
            LocalDate localDate = LocalDate.parse(source, dateFormatter);
            return localDate;
        }
    };

    Converter<LocalDate, String> localDateToStringConverter = new AbstractConverter<LocalDate, String>() {
        @Override
        protected String convert(LocalDate source) {
            return source.format(dateFormatter);
        }
    };
}