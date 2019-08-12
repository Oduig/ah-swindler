package com.github.oduig.auctionswindler.blizzard.generated

data class Auction(
    val auc: Int,
    val bid: Int,
    val bonusLists: List<BonusLists>,
    val buyout: Int,
    val context: Int,
    val item: Int,
    val modifiers: List<Modifier>,
    val owner: String,
    val ownerRealm: String,
    val petBreedId: Int,
    val petLevel: Int,
    val petQualityId: Int,
    val petSpeciesId: Int,
    val quantity: Int,
    val rand: Int,
    val seed: Int,
    val timeLeft: String
)