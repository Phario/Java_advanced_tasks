# Obserwacje

## Efekty ograniczenia pamięci maszyny wirtualnej

### -Xmx[value]m

Przy użyciu ograniczeń górnych pamięci efekty stały się widoczne dla plików .csv dopiero po użyciu wartości 128 (128MB)
Przy wyższych wartościach pliki .csv były ciągle ładowane z cache (oczywiście po uprzednim załadowaniu ich), co ciekawe, 
przy każdym ograniczeniu od 512MB w dół załadowanie jakiegokolwiek zdjęcia powodowało wyładowanie plików .csv.

### -Xms[value]m

Przy użyciu ograniczeń dolnych (1024MB) zdjęcia zaczęły się ładować z cache (1-3 zdjęć), pliki .csv nie zostawały wyładowane po załadowaniu pojedynczego zdjęcia.
Ograniczenie dolne od 512MB dało podobny efekt, jednak wyraźnie było widać, że trochę częściej ładowały się zdjęcia.
Aplikacja po użyciu opcji z większymi wartościami pamięci mniej się zacinała.

## Efekty zmiany algorytmu garbage collector'a

### -XX:-ShrinkHeapInSteps

Opcja pozwala na wyłączenie stopniowego zmniejszania sterty. Było to widoczne podczas obserwowania zużywanej przez program pamięci w menadżerze zadań.
Pamięć była zmniejszana bardziej gwałtownie.

### -XX:+ShrinkHeapInSteps

Przy wykorzystaniu tej opcji zużycie pamięci trzymało się o wiele bliżej zadanych 1024MB.

### -XX:+UseG1GC

Domyślny algorytm garbage collector'a.

### -XX:+UseParallelGC

Przy wykorzystaniu tego algorytmu załadowanie obrazka powodowało wyładowanie częsciowo plików .csv, gdzie wcześniej wyładowywały się wszystkie na raz.

### -XX:+UseSerialGC

W porównaniu do UseParallelGC widać było wolniejsze wyładowywanie plików .csv. Użycie UseParallelGC pozwalało na cache około 8 plików, UseSerialGC pozwalał na cache około 20 plików.

### -XX:+UseParNewGC - (nie ma możliwości uruchomienia)
