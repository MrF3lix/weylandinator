# Weylandinator

![Works](https://img.shields.io/badge/does%20it%20work%3F-totally-success)
[![GitHub license](https://img.shields.io/github/license/MrF3lix/weylandinator)](https://github.com/MrF3lix/weylandinator)

![Weylandinator](./img/weylandinator-logo.png)

## PoC - Schaltung

- UI
  - Schaltung definieren
    - Spannungsquelle
    - Wiederstände
    - Verbraucher
- Logik
  - Komplettwiederstand berechnen
  - Spannungsabfall pro Wiederstand

### Beispiel Schaltung

![Beispiel](./docs/beispiel.dio.svg)

### Datenstruktur

Circuit Objekt

```JSON
"circuit": {
    "elements": [
        {
            "start": "1",
            "end": "2",
            "type": "Spannungsquelle",
            "properties": [
                {
                    "key": "amount",
                    "value": "10",
                    "unit": "V"
                }
            ]
        },
        {
            "start": "2",
            "end": "3",
            "type": "Wiederstand",
            "properties": [
                {
                    "key": "amount",
                    "value": "500",
                    "unit": "Ohm"
                }
            ]
        },
        {
            "start": "Wiederstand",
            "end": "3",
            "type": "4",
            "properties": [
                {
                    "key": "amount",
                    "value": "1000",
                    "unit": "Ohm"
                }
            ]
        },
        {
            "start": "3",
            "end": "4",
            "type": "Wiederstand",
            "properties": [
                {
                    "key": "amount",
                    "value": "1000",
                    "unit": "Ohm"
                }
            ]
        }
    ]
}
```
