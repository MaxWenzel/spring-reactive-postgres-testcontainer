package org.devzone.addresses.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("postalcode")
public class Address  {

    @EqualsAndHashCode.Exclude
    @Id
    private Integer id;
    @Column("postalCode")
    private String postalCode;
    private String locality;
    @EqualsAndHashCode.Exclude
    private String state;

}
