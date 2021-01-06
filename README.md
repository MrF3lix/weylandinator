# Weylandinator ©

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/MrF3lix/weylandinator/CI?label=CI&logo=GitHUb)
[![GitHub issues](https://img.shields.io/github/issues/MrF3lix/weylandinator)](https://github.com/MrF3lix/weylandinator/issues)


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

### Beispiel Datenstruktur Circuit

```JSON
"circuit": {
    "elements": [
        {
            "start": "1",
            "end": "2",
            "type": "Spannungsquelle",
            "name": "U0",
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
            "name": "R1",
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
            "start": "3",
            "end": "4",
            "name": "R2",
            "type": "Wiederstand",
            "properties": [
                {
                    "key": "amount",
                    "value": "1000",
                    "unit": "Ohm"
                }
            ]
        },
        {
            "start": "4",
            "end": "5",
            "name": "R3",
            "type": "Wiederstand",
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
            "end": "5",
            "name": "R4",
            "type": "Wiederstand",
            "properties": [
                {
                    "key": "amount",
                    "value": "2000",
                    "unit": "Ohm"
                }
            ]
        }
    ]
}
```
