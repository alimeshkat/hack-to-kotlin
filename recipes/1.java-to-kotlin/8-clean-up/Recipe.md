# Clean up

In this recipe we will help you to clean up the code.  

---

## Clean up annotations

1) Now all the code is converted to Kotlin, there is no need for `@JvmField` or other annotation that were used to
   enable interoperability with `Java`. Go back and remove them all!
2) When ready, run all tests:
    
    ```shell
    (cd ../.. && ./mvnw clean verify -pl :java-to-kotlin)
    ```

[Go to the finish](../Finish.md)
