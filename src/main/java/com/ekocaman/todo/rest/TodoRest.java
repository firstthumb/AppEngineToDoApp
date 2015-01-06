package com.ekocaman.todo.rest;

import com.ekocaman.todo.entity.TodoEntity;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Api(
        name = "todo",
        version = "v1"
)
public class TodoRest {
    private static final String ENTITY_TODO = "TODO";
    private static final String ENTITY_TODO_ID = "id";
    private static final String ENTITY_TODO_TITLE = "title";
    private static final String ENTITY_TODO_COMPLETED = "completed";

    private Logger logger = LoggerFactory.getLogger(TodoRest.class);

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @ApiMethod(name = "todo.save", httpMethod = "post")
    public void save(TodoEntity todoEntity) {
        logger.debug("Save todo : " + todoEntity);
        Entity entity = new Entity(ENTITY_TODO);
        entity.setProperty(ENTITY_TODO_TITLE, todoEntity.getTitle());
        entity.setProperty(ENTITY_TODO_COMPLETED, todoEntity.isCompleted());
        datastore.put(entity);
    }

    @ApiMethod(name = "todo.list", httpMethod = "get")
    public List<TodoEntity> list() {
        logger.debug("List todo");
        List<TodoEntity> result = new ArrayList<>();

        Query query = new Query(ENTITY_TODO);
        PreparedQuery preparedQuery = datastore.prepare(query);
        for (Entity entity : preparedQuery.asIterable()) {
            result.add(new TodoEntity((String) entity.getProperty(ENTITY_TODO_ID), (String) entity.getProperty(ENTITY_TODO_TITLE), (Boolean) entity.getProperty(ENTITY_TODO_COMPLETED)));
        }

        return result;
    }
}
