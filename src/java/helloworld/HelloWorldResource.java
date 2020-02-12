/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package helloworld;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author mkuchtiak
 */

@Stateless
@Path("/greeting")
public class HelloWorldResource {

    @EJB
    private NameStorageBean nameStorage;
    /**
     * Retrieves representation of an instance of helloworld.HelloWorldResource
     * @return an instance of java.lang.String
     */
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getGreeting() {
        return nameStorage.getName();
    }*/

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGreeting() {
        JsonObject json = Json.createObjectBuilder().add("saludo", "Hi!").build();
        return Response.ok(json,MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * PUT method for updating an instance of HelloWorldResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    /*@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String setName(String content) {
        nameStorage.setName(content);
        return "Hi";
    }*/
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setName(String content) {
        nameStorage.setName(content);
        return Response.ok("Hi "+nameStorage.getName(),MediaType.APPLICATION_JSON).build();
    }
    
    
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMedotPost(JsonObject hi){
        String usuario = hi.getString("usuario");
        String contrasena = hi.getString("contrasena");
        if (usuario.equalsIgnoreCase("a") && contrasena.equalsIgnoreCase("a")) {
            JsonObject json = null; 
            String key = "clave";
            long tiempo = System.currentTimeMillis();
            String jwt = Jwts.builder()
                             .signWith(SignatureAlgorithm.HS256, key)
                             .setSubject("Mauricio lopez")
                             .setIssuedAt(new Date(tiempo))
                             .setExpiration(new Date(tiempo+900000))
                             .claim("status", "success")
                             .compact();
            json = Json.createObjectBuilder().add("jwt", jwt).build();
            return Response.status(Response.Status.CREATED).entity(json).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        /*if (usuario.equalsIgnoreCase("a") && contrasena.equalsIgnoreCase("a")) {
            json = Json.createObjectBuilder().add("status", "success").build();
            return Response.ok(json,MediaType.APPLICATION_JSON).build();
        } else {
            json = Json.createObjectBuilder().add("status", "error").build();
            return Response.ok(json,MediaType.APPLICATION_JSON).build();
        }*/
         
                
        
        //return Response.ok("Hi "+nameStorage.getName() ,MediaType.APPLICATION_JSON).build();
    }
}
