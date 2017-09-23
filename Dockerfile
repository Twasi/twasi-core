FROM java:8
ADD /target/TwasiCore-1.0-SNAPSHOT-jar-with-dependencies.jar twasi-core.jar
ENTRYPOINT ["java","-jar","twasi-core.jar"]