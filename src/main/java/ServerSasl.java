/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import java.util.Hashtable;

/**
 * Demonstrates how to discover a server's supported SASL mechanisms.
 *
 * usage: java ServerSasl url username password
 */
class ServerSasl {
    public static void main(String[] args) {

      if (args.length < 3) {
        System.err.println("Three required arguments: url username password");
        System.exit(1);
      }
      String url = args[0];
      String username = args[1];
      String password = args[2];
      System.out.println("Connecting to " + url);
      try {
        // Create initial context
        DirContext ctx = connect(url, username, password);
        System.out.println("Authentication succeeded!");

        try {
          System.out.println(supportedSaslMechanisms(ctx));
        } finally {
          // Close the context when we're done
          ctx.close();
        }
      } catch (NamingException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    /** Opens an LDAP context, authenticating with a simple bind. */
    static DirContext connect(String url, String username, String password) throws NamingException {
      Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL, url);
      env.put(Context.SECURITY_PRINCIPAL, username);
      env.put(Context.SECURITY_CREDENTIALS, password);
      //env.put(Context.REFERRAL, "follow");
      return new InitialDirContext(env);
    }

    /** Reads supportedSASLMechanisms from the root DSE. */
    static Attributes supportedSaslMechanisms(DirContext ctx) throws NamingException {
      return ctx.getAttributes("", new String[]{"supportedSASLMechanisms"});
    }
}
