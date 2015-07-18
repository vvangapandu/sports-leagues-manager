/*
 * This software is the confidential and proprietary information of
 * eharmony.com and may not be used, reproduced, modified, distributed,
 * publicly displayed or otherwise disclosed without the express written
 * consent of eharmony.com.
 *
 * This software is a work of authorship by eharmony.com and protected by
 * the copyright laws of the United States and foreign jurisdictions.
 *
 * Copyright 2000-2015 eharmony.com, Inc. All rights reserved.
 *
 */
package com.eharmony.services.configservice;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider public class IllegalArgumentMapper
          implements ExceptionMapper<IllegalArgumentException> {

    public Response toResponse(IllegalArgumentException ex) {

        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(ex.getMessage())
                       .type(MediaType.TEXT_PLAIN_TYPE)
                       .build();

    }

}
