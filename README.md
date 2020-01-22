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

## Utilisation de l'updater

Commencez par créer votre Updater

`Updater updater = new Updater(new URL(DL_URL), MC_GAMEDIR, MC_VERSION);`

Remplacez l'adresse par celle de votre serveur kMCLauncher web, la variable GAMEDIR référence le chemin d'accès où vous souhaitez installer Minecraft.
Pour la version il faudra la déclarer en String, cette données permet de récupérer les informations de version disponible sur l'[API de Mojang][1].

[1]: https://launchermeta.mojang.com/mc/game/version_manifest.json

Une fois votre Updater créé vous pouvez vérifier les fichiers indésirable dans les dossiers de votre choix.

`updater.removeBadFile("mods")`

Ensuite il ne vous reste plus qu'à lancer les mises à jour, elles sont donc divisées en deux, la partie Mojang et votre partie privé.

`updater.updateMojang();`

`updater.updateServer();`
## Utilisation du configurator

Pour vous aidez dans la gestion de la configuration de votre Launcher le Configurator est disponible et vous permets de sauvegarder les variable suivantes : 

- Gestion de la RAM
- Résolution de base
- Affichage de la console ou non
- Sauvegarde des identifiants ou non

Par la suite il pourras être configurable pour stocker vos propres données.

Vous pouvez le créer en précisant l'emplacement du jeu ainsi que le nom du fichier de sauvegarde souhaité.

Attention toutes les données stockées sont en clair sous le format json.

`Configurator config = new Configurator(MC_GAMEDIR, "launcher_options.json");`

Vous pouvez lui envoyer votre MinecraftUser authentifié pour le sauvegarder la suite.

`config.setUser(user);`

Pour lancer une sauvegarde de la configuration

`config.save();`

Pour récupérer les options il vous faudra passer par JSON

`config.getParams().getInt("minRam");`

Vous pouvez également éditer ces options

`config.setParamInt("minRam", 1024);`
`config.setParamBoolean("saveAuth", false);`

Ci dessous la liste des options et leurs types :

Variable | Type | Description
---- | :--: | :--:
minRam | int | Ram minimum alloué à java par le Launcher
maxRam | int | Ram maximum alloué à java par le Launcher 
resY | int | La hauteur de la fenêtre de jeu
resX | int | La largeur de la fenêtre de jeu
saveAuth | boolean | Si le Configurator doit ou non sauvegarder les identifiants
console | boolean | Si le Launcher doit afficher une console au lancement du jeu



## Utilisation de l'authentificateur

Vous pouvez créer votre MinecraftUser via le Configurator. Si un utilisateur enregistré est disponible il se connecteras directement.

`MinecraftUser user = config.getUser();`

Si l'utilisateur n'a pas enregistré son compte il faudra l'authentifier via un formulaire comme suis.

`user.auth(USERNAME, PASSWORD, true);`

Cette fonction vous retourneras true si tous c'est les identifiants sont correcte.

Vous pouvez également à tous moment vérifier si la session est encore active via la fonction isAuth()

`user.isAuth();`

## Utilisation du Launcher

Il vous faut créer votre class Launcher comme suis

`Launcher launcher = new Launcher(LaunchType.Vanilla, MC_GAMEDIR, MC_VERSION, user);`

Et ensuite lancer Minecraft

`launcher.run();`

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