package it.classhidra.core.tool.jaas_authentication.handler;

import javax.security.auth.callback.*;

public class PassiveCallbackHandler implements CallbackHandler {

    private String username;
    char[] password;


    public PassiveCallbackHandler(String user, String pass) {
        this.username = user;
        this.password = pass.toCharArray();
    }

    public void handle(Callback[] callbacks)
        throws java.io.IOException, UnsupportedCallbackException
    {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                ((NameCallback)callbacks[i]).setName(username);
            } else if (callbacks[i] instanceof PasswordCallback) {
                ((PasswordCallback)callbacks[i]).setPassword(password);
            } else {
                throw new UnsupportedCallbackException(
                            callbacks[i], "Callback class not supported");
            }
        }
    }

    public void clearPassword() {
        if (password != null) {
            for (int i = 0; i < password.length; i++)
                password[i] = ' ';
            password = null;
        }
    }

}

