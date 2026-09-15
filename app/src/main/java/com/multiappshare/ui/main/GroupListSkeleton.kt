package com.multiappshare.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.multiappshare.R
import com.multiappshare.ui.theme.Dimens

@Composable
fun GroupListSkeleton(modifier: Modifier = Modifier) {
    val loading = stringResource(R.string.cd_loading_groups)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Space4)
            .semantics { contentDescription = loading },
    ) {
        repeat(3) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.Space1),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Spacer(modifier = Modifier.height(Dimens.Avatar))
            }
        }
    }
}
