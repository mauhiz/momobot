/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of JBouncer.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: User.java,v 1.2 2004/03/01 19:13:37 pjm2 Exp $

 */
package bouncer;

/**
 * @author mauhiz
 */
public class User {
    /**
     * 
     */
    private String login;
    /**
     * 
     */
    private String password;

    /**
     * @param login1
     * @param password1
     */
    public User(final String login1, final String password1) {
        this.login = login1;
        this.password = password1;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        User other = (User) o;
        return other.login.equals(this.login) && other.password.equals(this.password);
    }

    /**
     * @return
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.login.hashCode() + this.password.hashCode();
    }
}
