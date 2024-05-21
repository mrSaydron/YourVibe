package ru.mrak.yourvibe.model

data class Root(
    var userName: UserName,
    var users: HashMap<String, User>,
    var vibes: HashMap<String, Vibe>,
    var vibeOwner : HashMap<String, List<String>>,
    var vibeChoiceTrue: HashMap<String, List<String>>,
    var vibeChoiceFalse: HashMap<String, List<String>>,
)
