package com.toast.user.domain.vo;

import com.toast.shared.domain.types.Nothing;
import com.toast.shared.domain.types.result.Result;
import com.toast.user.domain.UserError;

import java.util.Objects;

public final class Name {
    private final String firstName;
    private final String lastName;
    
    private Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public static Result<UserError, Name> of(String firstName, String lastName) {
        return validateString(firstName, "First name")
                       .combine(validateString(lastName, "Last name"))
                       .mapTo(() -> new Name(firstName, lastName));
    }
    
    public static Name load(String firstName, String lastName) {
        return of(firstName, lastName).orElseThrow(() -> new IllegalArgumentException("Invalid name"));
    }
    
    private static Result<UserError, Nothing> validateString(String value, String field) {
        return Result.<UserError, String>success(value)
                       .filter(Objects::nonNull, new UserError.InvalidName(field + " is required"))
                       .mustBe(v -> !v.isBlank(), new UserError.InvalidName(field + " cannot be empty"))
                       .mustBe(v -> v.length() <= 50, new UserError.InvalidName(field + " is too long"))
                       .mapTo(Nothing::new);
    }
    
    public String firstName() {
        return firstName;
    }
    
    public String lastName() {
        return lastName;
    }
    
    @Override
    public String toString() {
        return "Name{" +
                       "firstName='" + firstName + '\'' +
                       ", lastName='" + lastName + '\'' +
                       '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Name name)) return false;
        return Objects.equals(firstName, name.firstName) && Objects.equals(lastName, name.lastName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
