package com.rbkmoney.adapter.atol.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    @ToString.Exclude
    private String login;

    @ToString.Exclude
    private String pass;

}
