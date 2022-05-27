package br.edu.ifsp.scl.ads.pdm.pedrapapeltesoura

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Setting(val numberPlayers: Int = 2): Parcelable
