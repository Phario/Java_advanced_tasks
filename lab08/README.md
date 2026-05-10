# Raport z testowania czasu wykonywania metod natywnych oraz standardowych
Wygenerowanie .h  
```aiignore
javac -h . GaussianFilter.java                                                                                                        
```
Skompilowanie pliku źródłowego
```
gcc -shared -o gaussian.dll gaussian.c `
   -I"$env:JAVA_HOME\include" `                                                                                                                                                                                                   
   -I"$env:JAVA_HOME\include\win32"
```
Uruchomienie aplikacji:
```aiignore
mvn clean package
java "-Djava.library.path=." -jar app/target/app-1.0-SNAPSHOT.jar
```

Biblioteki natywne skompilowano z flagą -O3  
Uruchomienie programu odbywa się za pomocą opcji
```aiignore
-XX:+PrintCompilation
```
która informuje o włączeniu się kompilatora JIT
Badania wykonano dla obrazka z kotem o rozdzielczości 778x1182

Czas [ms] dla filtra Gaussa (promień = 10)

| Ilość iteracji | Natywne | Standardowe |
|----------------|---------|-------------|
| 1              | 62      | 61          |
| 10             | 210     | 325         |
| 50             | 865     | 1500        |
| 90             | 1500    | 2650        |

Badania dla obrazu o rozdzielczości 4000x6000

| Ilość iteracji | Natywne | Standardowe |
|----------------|---------|-------------|
| 1              | 2850    | 2600        |
| 2              | 3307    | 3485        |
| 4              | 5482    | 6064        |
| 90             | 11080   | 12105       |