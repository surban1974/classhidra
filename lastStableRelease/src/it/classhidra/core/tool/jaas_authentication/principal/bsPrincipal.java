package it.classhidra.core.tool.jaas_authentication.principal;

import java.security.Principal;


public class bsPrincipal implements Principal, java.io.Serializable {

	private static final long serialVersionUID = 4618327668504071021L;
	private String name;

    /**
     * Create a <code>RdbmsPrincipal</code> with no
     * user name.
     *
     */
    public bsPrincipal() {
        name = "";
    }


    public bsPrincipal(String newName) {
        name = newName;
    }

    public boolean equals(Object o) {

        if (o == null)
            return false;

        if (this == o)
            return true;
 
        if (o instanceof bsPrincipal) {
            if (((bsPrincipal) o).getName().equals(name))
                return true;
            else
                return false;
        }
        else 
            return false;
    }

 
    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}

