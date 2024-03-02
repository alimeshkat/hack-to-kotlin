# Application configuration setup guide

In the java-to-kotlin-complete module, we had configured H2 database and JPA. Now we won't be using JPA anymore, and we will be using  R2DBC.


```yaml
spring:
  profiles:
    active: local
server:
  port: 0

## Just for debugging purposes
logging:
  level:
    io:
      r2dbc: debug
```

[Go back to the recipe](../../1.java-to-kotlin/1-project-setup/Recipe.md)
