tor, anonümiseeriv võrk
=======================

Tutorial, kuidas käivitada programmi
------------------------------------

1. Käivita RegistryStarter kaustas "src/registry"

2. Käivita tester1, tester2 ja tester3 kaustas "src/node"

3. Sisesta URL. Kui kopeerid kusagilt, siis pane URL lõppu tühik, et IntelliJ seda brauseris ei avaks.

4. Fail/failid (lokaalvõrgus testimisel failid) salvestatakse projekti kausta "tor"

-----------------------------------------------------------------
Lisainfo
--------
1. "tor" kaustas on pordinumbritega tekstifailid, kuhu iga node paneb kirja oma requestide unikaalsed id väärtused. Kuna testima pidi ka lokaalvõrgus, siis on kaustas 3 faili.
2. "util" kaustas on faili kirjutamiseks ja sealt lugemiseks mõeldud klassid
3. "tor selgitus.jpg" on pilt sellest, mille õpetaja Verrev klassis tahvlile tegi
4. "resources" kaustas on config.properties failid
-----------------------------------------------------------------
Gerdi soovitused
--------
- retry 
- high lazyness testing
- veateade et ei saanud site kätte pärast mingit aega

