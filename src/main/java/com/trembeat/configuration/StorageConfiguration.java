package com.trembeat.configuration;

import com.trembeat.domain.models.*;
import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.convert.converter.Converter;

import java.io.File;

@Configuration
public class StorageConfiguration {
    @Bean
    public FilesystemStoreConfigurer filesystemStoreConfigurer() {
        String basePath = getClass().getClassLoader().getResource(".").getFile();

        FilesystemStoreConfigurer configurer = registry -> {
            // TODO: proper file format and MIME type handling

            String soundsPath = String.format("%sstatic/uploads/sounds/", basePath);
            new File(soundsPath).mkdirs();
            registry.addConverter(new Converter<Sound, String>() {
                @Override
                public String convert(Sound source) {
                    return String.format("%s%d.mp3", soundsPath, source.getId());
                }
            });

            String ppPath = String.format("%sstatic/uploads/profile-pictures/", basePath);
            new File(ppPath).mkdirs();
            registry.addConverter(new Converter<ProfilePicture, String>() {
                @Override
                public String convert(ProfilePicture source) {
                    return String.format("%s%d.jpeg", soundsPath, source.getId());
                }
            });
        };

        return configurer;
    }
}
