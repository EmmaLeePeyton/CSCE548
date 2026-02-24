# Run the PantryServiceApi on localhost:7070
# Platform: Windows PowerShell + Maven + Javalin

mvn clean compile
mvn exec:java "-Dexec.mainClass=PantryServiceApi"