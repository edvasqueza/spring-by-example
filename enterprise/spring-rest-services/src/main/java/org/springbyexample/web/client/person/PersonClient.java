/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springbyexample.web.client.person;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springbyexample.web.client.RestClient;
import org.springbyexample.web.service.person.PersonMarshallingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


/**
 * Person client.
 * 
 * @author David Winterfeldt
 */
@Component
public class PersonClient implements PersonMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final RestClient client;

    @Autowired
    public PersonClient(RestClient client) {
        this.client = client;
    }

    @Override
    public PersonResponse findById(long id) {
        PersonResponse response = null;
        
        String url = client.getUrl(FIND_BY_ID_REQUEST);
        
        logger.debug("REST client findById.  id={}  url='{}'", id, url);
        
        Map<String, Long> vars = Collections.singletonMap(ID_VAR, id);
        
        response = client.getRestTemplate().getForObject(url, PersonResponse.class, vars);
        
        return response;
    }

    @Override
    public PersonFindResponse find(int page, int pageSize) {
        PersonFindResponse response = null;
        
        String url = client.getUrl(FIND_PAGINATED_REQUEST);
        
        logger.debug("REST client paginated find.  page={}  pageSize={}  url='{}'", 
                     new Object[] { page, pageSize, url});

        Map<String, Integer> vars = new HashMap<String, Integer>();
        
        vars.put(PAGE_VAR, page);
        vars.put(PAGE_SIZE_VAR, pageSize);

        response = client.getRestTemplate().getForObject(url, PersonFindResponse.class, vars);
        
        return response;
    }

    @Override
    public PersonFindResponse find() {
        PersonFindResponse response = null;
        
        String url = client.getUrl(FIND_REQUEST);
        
        logger.debug("REST client find. url='{}'", url);

        response = client.getRestTemplate().getForObject(url, PersonFindResponse.class);
        
        return response;
    }

    @Override
    public PersonResponse save(Person request) {
        PersonResponse response = null;
        
        String url = client.getUrl(SAVE_REQUEST);
        
        logger.debug("REST client save.  id={}  url='{}'", request.getId(), url);

        response =  client.getRestTemplate().postForObject(url, request, PersonResponse.class);
        
        return response;
    }

    @Override
    public ResponseResult delete(long id) {
        ResponseResult response = null;
        
        String url = client.getUrl(DELETE_REQUEST);
        
        logger.debug("REST client delete.  id={}  url='{}'", id, url);
        
        Map<String, Long> vars = Collections.singletonMap(ID_VAR, id);
        
        response = client.getRestTemplate().exchange(url, HttpMethod.DELETE, null, ResponseResult.class, vars).getBody();

        return response;
    }

}