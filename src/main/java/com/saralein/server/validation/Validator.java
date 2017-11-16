package com.saralein.server.validation;

import java.util.ArrayList;
import java.util.List;

public interface Validator {
    ArrayList<String> validate(List<String> args);
}
