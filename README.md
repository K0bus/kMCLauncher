# kMCLauncher
kMCLauncher est une nouvelle librairie Java permettant aux développeurs et créateurs de serveur de pouvoir ce lancer dans la création de launcher Custom facilement et rapidement.

Ce projet reprend le principe des librairies créées par [@Litarvan][0].

[0]: https://github.com/Litarvan

Cette librairie a pour différences l'utilisation des ressources de Mojang pour la pluspart des téléchargements.
L'avantage étant de ne pas avoir à récupérer les assets / librairies de Minecraft sur votre serveur pour les faires télécharger.

A venir cette librairie permettras également d'authentifier les joueurs pour lancer le jeu ou de le lancer en version non-officiel.

## Prérequis

- Java
- Serveur web (Pour l'installation du serveur de mise à jour)

## Utilisation

Une fois la librairies ajouter à votre "Buildpath" dans votre projet, il vous faudra créer l'updater.

`Updater updater = new Updater(new URL("http://127.0.0.1/kMCUpdate-Server/"),GAMEDIR, "1.12.2");`

Remplacez l'adresse par celle de votre serveur kMCLauncher web, la variable GAMEDIR référence le chemin d'accès où vous souhaitez installer Minecraft.
Pour la version il faudra la déclarer en String, cette données permet de récupérer les informations de version disponible sur l'[API de Mojang][1].

[1]: https://launchermeta.mojang.com/mc/game/version_manifest.json

Une fois votre Updater créé il ne vous reste plus qu'à lancer les mises à jour, elles sont donc divisées en deux, la partie Mojang et votre partie privé.

`updater.updateMojang();`

`updater.updateServer();`

## Version de Minecraft testé

Vous trouverez ci dessous les versions testé, n'hésitez pas à me remonter les versions que vous avez vous même testé pour remplir ce tableau au mieux possible !

Version | Vanilla | Forge
---- | :--: | :--:
1.15 | | 
1.14 | | 
1.13 | | 
1.12 | :white_check_mark: | :white_check_mark:
1.11 | | 
1.10 | | 
1.9 | | 
1.8 | | 
1.7 | | 
1.6 | | 
1.5 | | 
1.4 | | 
1.3 | | 
1.2 | | 
1.1 | | 
1.0 | | 

## 