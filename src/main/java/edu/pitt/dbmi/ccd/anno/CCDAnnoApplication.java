/*
 * Copyright (C) 2015 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package edu.pitt.dbmi.ccd.anno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

import edu.pitt.dbmi.ccd.db.CCDDatabaseApplication;
import edu.pitt.dbmi.ccd.security.CCDSecurityApplication;

import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Mark Silvis
 */
@SpringBootApplication
@Import({CCDDatabaseApplication.class, CCDSecurityApplication.class})
@EnableEntityLinks
@EnableHypermediaSupport(type=HypermediaType.HAL)
@EnableSwagger2
public class CCDAnnoApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(CCDAnnoApplication.class, args);
    }

//    @Bean
//    public SecurityConfiguration securityInfo() {
//        return new SecurityConfiguration("curl", "", "", "", "", ApiKeyVehicle.HEADER, "Authorization", ",");
//    }
//
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

//            .securitySchemes(securitySchemes())
//            .securityContexts(securityContexts());
//    }
//
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("CCD Annotations")
            .description("Center for Causal Discovery Annotations and Provenance Management API")
            .version("0.3.0")
            .licenseUrl("https://github.com/bd2kccd/ccd-anno-api/blob/develop/LICENSE")
            .build();
    }
//
//    private List<SecurityScheme> securitySchemes() {
////        final List<SecurityScheme> authorizationTypes = new ArrayList<>();
////        final List<AuthorizationScope> authorizationScopes = Arrays.asList(authorizationScopes());
////        final List<GrantType> grantTypes = new ArrayList<>();
////
////        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint("/api/oauth/token", "client_id", "client_secret");
////        TokenEndpoint tokenEndpoint = new TokenEndpoint("/api/oauth/token", "token");
////
////        PasswordTokenRequestEndpoint passwordTokenRequestEndpoint = new PasswordTokenRequestEndpoint("/api/oauth/token", "client_id", "client_secret", "user", "password");
////        grantTypes.add(new OAuth2PasswordCredentialsGrantType(passwordTokenRequestEndpoint));
////        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
////
////        authorizationTypes.add(new OAuth("oauth2", authorizationScopes, grantTypes));
////
////        return authorizationTypes;
//
//        return Arrays.asList(
//            new OAuthBuilder()
//                .name("ccd-annotations-auth")
//                .grantTypes(grantTypes())
//                .scopes(scopes())
//                .build());
//    }
//
//    private List<AuthorizationScope> scopes() {
//        return Arrays.asList(
//            new AuthorizationScope("read", "read only"),
//            new AuthorizationScope("write", "read and write")
//        );
//    }
//
//    private List<GrantType> grantTypes() {
//        return Arrays.asList(
//            new ImplicitGrantBuilder()
//                .loginEndpoint(new LoginEndpoint("http://localhost:8080/api/oauth/dialog"))
//                .build()
//        );
//    }
//
//    private List<SecurityContext> securityContexts() {
//        final AuthorizationScope[] authorizationScopes = scopes().toArray(new AuthorizationScope[0]);
//        return Arrays.asList(
//            SecurityContext.builder()
//                .securityReferences(
//                    Arrays.asList(SecurityReference.builder()
//                        .reference("test")
//                        .scopes(authorizationScopes)
//                        .build());));
//    }

//    private List<SecurityContext> securityContexts() {
//        return Arrays.asList(SecurityContext.builder()
//            .securityReferences(securityReferences())
//            .build());
//    }
//
//    private List<SecurityReference> securityReferences() {
//        final List<SecurityReference> references = new ArrayList<>();
//
//        AuthorizationScope[] authorizationScopes = authorizationScopes();
//
//        references.add(new SecurityReference("USER", authorizationScopes));
//        references.add(new SecurityReference("ADMIN", authorizationScopes));
//        return references;
//    }
//
//    private AuthorizationScope[] authorizationScopes() {
//        return new AuthorizationScope[]{
//            new AuthorizationScope("read", "read only"),
//            new AuthorizationScope("write", "read and write")
//        };
//    }
//
//    private class PasswordTokenRequestEndpoint extends TokenRequestEndpoint {
//        private final String username;
//        private final String password;
//
//        public PasswordTokenRequestEndpoint(String url, String clientId, String clientSecret, String username, String password) {
//            super(url, clientId, clientSecret);
//            this.username = username;
//            this.password = password;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//    }
//
//    private class OAuth2PasswordCredentialsGrantType extends GrantType {
//        private final PasswordTokenRequestEndpoint tokenRequestEndpoint;
//
//        public OAuth2PasswordCredentialsGrantType(PasswordTokenRequestEndpoint tokenRequestEndpoint) {
//            super("password");
//            this.tokenRequestEndpoint = tokenRequestEndpoint;
//        }
//    }
}
