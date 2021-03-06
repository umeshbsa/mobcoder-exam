package com.mobcoder.exam.user;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Field;
import com.mobcoder.exam.utils.Validation;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "create, update, delete and get user data", tags = {"User"})
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ProfileDto.class)
    })
    @RequestMapping(
            value = "/v1/user/login",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> userSignUp(

            @ApiParam(
                    name = "email",
                    type = "String",
                    required = true)
            @RequestParam String email,

            @ApiParam(
                    name = "password",
                    type = "String",
                    required = true)
            @RequestParam String password) {

        try {
            if (email == null || email.length() == 0) {
                return Validation.getFieldValid(Field.FIELD_EMAIL);
            } else if (password == null || password.length() == 0) {
                return Validation.getFieldValid(Field.FIELD_PASSWORD);
            } else {

                int passwordLength = password.length();
                if (passwordLength >= 6 && passwordLength <= 10) {
                    User user = new User();
                    user.username = email;
                    return userService.userSignUp(user, password);
                } else {
                    Validation.getErrorValid(Errors.PASSWORD_6_10, Code.PASSWORD_6_10);
                }
            }
        } catch (Exception e) {

            System.out.println("EEEEEE " + e.getMessage());
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }


    @ApiOperation(value = "delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ProfileDto.class)
    })
    @RequestMapping(value = "/v1/user/delete",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse> deleteUser(
            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token
    ) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return userService.deleteUser(auth.getName());
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

}
