package com.roily.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

/**
 * descripte:
 *
 * @author: RoilyFish
 * @date: 2022/2/15
 */
@ConfigurationProperties(prefix = "roilyfish")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class roilyfish {
    String name;
}
