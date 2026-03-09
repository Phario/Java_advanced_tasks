# Uruchomienie aplikacji za pomocą linii komend

Kompilacja kodu źródłowego
```
javac --module-source-path "C:\Users\grzed\IdeaProjects\gdyn280963_javatz_2026\lab01\*\src\main\java" -d out -m Zipper,App
```

Wymagane jest skopiowanie pliku App.fxml do folderu out
```
copy C:\Users\grzed\IdeaProjects\gdyn280963_javatz_2026\lab01\App\src\main\resources\App.fxml out\App\App.fxml
```

Spakowanie w pliki jar
```
jar --create --file mods\Zipper.jar -C out\Zipper .
jar --create --file mods\App.jar -C out\App .
```

Generowanie obrazu runtime'u javy
```
jlink --module-path "mods;$env:JAVA_HOME\jmods" --add-modules App,Zipper,javafx.graphics,javafx.fxml,javafx.controls --output outputdir
```

Odpalenie programu
```
outputdir\bin\java --module App/pl.pwr.ite.dynak.gui.Main
```