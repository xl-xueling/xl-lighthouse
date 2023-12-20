package com.dtstep.lighthouse.insights.modal;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class Group implements Serializable {

    private Integer id;

    @NotEmpty
    private String token;

    private String columns;


}
