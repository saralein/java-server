package com.saralein.server.exchange;

public class Cookie {
    private final String name;
    private final String value;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cookie)) {
            return false;
        }

        Cookie cookie = (Cookie) object;
        return name.equals(cookie.getName()) && value.equals(cookie.getValue());
    }
}
