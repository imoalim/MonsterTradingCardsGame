class User {
- username: string
- password: string
+ register()
+ login()
+ manageCards()
+ buyCards()
+ defineDeck()
+ battle()
+ compareStats()
  }

class Card {
- name: string
- damage: int
- elementType: string
  }

class SpellCard {
- attack(elementType: string): int
  }

class MonsterCard {
- attack(): int
  }

class Stack {
- cards: List<Card>
+ addCard(card: Card)
+ removeCard(card: Card)
  }

class Package {
- cards: List<Card>
+ acquirePackage(): List<Card>
  }

class Deck {
- cards: List<Card>
+ selectCards(cards: List<Card>)
  }

class Battle {
- player1: User
- player2: User
- rounds: int
- log: string
+ playRound(): string
  }

class Scoreboard {
- users: List<User>
+ updateStats(user: User, result: string)
  }

class ProfilePage {
- user: User
+ editProfile()
  }

class Stats {
- user: User
- elo: int
+ updateElo(result: string)
  }

class Security {
- token: string
+ checkToken(token: string)
  }

User --> Stack
User --> Deck
User --> Battle
User --> Scoreboard
User --> ProfilePage
User --> Stats
User --> Security

Card <|-- SpellCard
Card <|-- MonsterCard

Stack "1" *-- "*" Card
Package "1" *-- "*" Card
Deck "1" *-- "*" Card
Battle "2" *-- "1" User
Scoreboard "1" *-- "*" User
ProfilePage "1" *-- "1" User
Stats "1" *-- "1" User
Security "1" *-- "1" User

This class diagram represents the entities and their relationships in the trading and battling card-game platform. It includes classes such as User, Card, SpellCard, MonsterCard, Stack, Package, Deck, Battle, Scoreboard, ProfilePage, Stats, and Security. The relationships between the classes are also defined, such as the associations between User and Stack, User and Deck, User and Battle, User and Scoreboard, User and ProfilePage, User and Stats, and User and Security. Additionally, the inheritance relationship between Card and its subclasses SpellCard and MonsterCard is also shown.