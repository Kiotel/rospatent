package com.example.rospatent


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import com.example.rospatent.classes.Patent


@Composable
fun FoundScreen(
  modifier: Modifier = Modifier,
  search: String,
  onSearchChange: (String) -> Unit,
  onBack: () -> Unit,
  onSubmit: () -> Unit,
  patents: List<Patent>,
  sortOption: String,
  onSortOptionChange: (String) -> Unit,
  onLoadMore: () -> Unit,
  isEnd: Boolean,
) {
  var isSettings by rememberSaveable { mutableStateOf(false) }
  var isChosenPatent by rememberSaveable { mutableStateOf(false) }
  var chosenPatent by remember {
    mutableStateOf(
      Patent()
    )
  }
  Column(modifier = modifier) {
    when (isChosenPatent) {
      false,
      -> {
        PatentsList(
          search = search,
          onSearchChange = onSearchChange,
          isSettings = isSettings,
          onSortScreenClose = { isSettings = it },
          onPatentChose = { patent ->
            chosenPatent = patent
            isChosenPatent = true
          },
          onBack = onBack,
          onSubmit = { onSubmit() },
          patents = patents,
          sortOption = sortOption,
          onSortOptionChange = { onSortOptionChange(it) },
          onLoadMore = onLoadMore,
          isEnd = isEnd
        )
      }

      true -> {
        PatentResult(patent = chosenPatent, onBack = { isChosenPatent = false })
      }
    }
  }
}

@Composable
fun SortScreen(
  modifier: Modifier = Modifier,
  onClose: () -> Unit,
  sortOption: String,
  onSortOptionChange: (String) -> Unit,
) {
  Dialog(onDismissRequest = onClose) {
    Card(
      modifier = modifier.padding(horizontal = 20.dp)
    ) {

      Column(
        modifier = Modifier
          .height(200.dp)
          .background(Color.White, shape = RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center
      ) {
        FilterOption(
          text = stringResource(R.string.filter_option_by_relevance),
          onClick = { onSortOptionChange(it) },
          selectedOption = sortOption,
          optionValue = "relevance",
        )
        FilterOption(text = stringResource(R.string.filter_option_by_publication_date),
          onClick = { onSortOptionChange(it) },
          selectedOption = sortOption,
          optionValue = "publication_date:desc",
          trailingIcon = {
            Icon(
              imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = null
            )
          })
        FilterOption(text = stringResource(R.string.filter_option_by_publication_date),
          onClick = { onSortOptionChange(it) },
          selectedOption = sortOption,
          optionValue = "publication_date:asc",
          trailingIcon = {
            Icon(
              imageVector = Icons.Outlined.KeyboardArrowUp, contentDescription = null
            )
          })
        FilterOption(text = stringResource(R.string.filter_option_by_filling_date),
          onClick = { onSortOptionChange(it) },
          selectedOption = sortOption,
          optionValue = "filing_date:desc",
          trailingIcon = {
            Icon(
              imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = null
            )
          })
        FilterOption(text = stringResource(R.string.filter_option_by_filling_date),
          onClick = { onSortOptionChange(it) },
          selectedOption = sortOption,
          optionValue = "filing_date:asc",
          trailingIcon = {
            Icon(
              imageVector = Icons.Outlined.KeyboardArrowUp, contentDescription = null
            )
          })
      }
    }
  }
}

@Composable
fun FilterOption(
  modifier: Modifier = Modifier,
  text: String,
  onClick: (String) -> Unit,
  selectedOption: String,
  optionValue: String,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  Row(
    modifier = modifier
      .clickable(onClick = { onClick(optionValue) })
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 5.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row {
      Text(
        text = text,
        fontWeight = if (selectedOption == optionValue) FontWeight.W800 else null,
      )
      if (trailingIcon != null) {
        trailingIcon()
      }
    }
    if (selectedOption == optionValue) {
      Icon(
        imageVector = Icons.Outlined.Check,
        contentDescription = null,
      )
    }
  }
}

@Composable
fun PatentCard(modifier: Modifier = Modifier, patent: Patent) {
  Card(modifier = modifier) {
    Row(
      verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
      Icon(painter = painterResource(id = R.drawable.patent_list_icon), contentDescription = null)
      Column {
        Text(
          text = patent.snippet.title, overflow = TextOverflow.Ellipsis, maxLines = 1
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = patent.snippet.inventor, fontSize = 3.em)
          Text(text = patent.common.application.number, fontSize = 3.em)
          Text(text = patent.snippet.classification.ipc, fontSize = 3.em)
        }
      }
    }
  }
}

@Composable
fun PatentsList(
  modifier: Modifier = Modifier, search: String,
  onSearchChange: (String) -> Unit,
  isSettings: Boolean,
  onSortScreenClose: (Boolean) -> Unit,
  onPatentChose: (Patent) -> Unit,
  onBack: () -> Unit,
  onSubmit: () -> Unit,
  patents: List<Patent>,
  sortOption: String,
  onSortOptionChange: (String) -> Unit,
  onLoadMore: () -> Unit,
  isEnd: Boolean,
) {
  Icon(modifier = Modifier
    .clickable { onBack() }
    .size(40.dp),
    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
    contentDescription = "back",
    tint = Color.Black)
  Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 0.dp)) {
    TextField(modifier = Modifier
      .fillMaxWidth()
      .padding(top = 20.dp),
      search = search,
      onSearchChange = { onSearchChange(it) },
      isDark = true,
      onSubmit = { onSubmit() },
      trailingIcon = {
        Icon(
          modifier = Modifier.clickable { onSearchChange("") },
          imageVector = Icons.Outlined.Clear,
          contentDescription = "clear search"
        )
      })
    if (!isEnd) {
      Button(onClick = { onLoadMore() }) {
        Text(text = "Загрузить ещё")
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {

      var isDropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }
      var dropDownMenuSelection by rememberSaveable { mutableStateOf("option1") }
      Box(modifier = Modifier.width(200.dp)) {
        OutlinedButton(
          onClick = { isDropDownMenuExpanded = true }, shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = dropDownMenuSelection)
            Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = null)
          }
        }
        DropdownMenu(
          expanded = isDropDownMenuExpanded,
          onDismissRequest = { isDropDownMenuExpanded = false }) {
          DropdownMenuItem(text = { Text("option1") }, onClick = {
            dropDownMenuSelection = "option1"
            isDropDownMenuExpanded = false
          })
          DropdownMenuItem(text = { Text("option2") }, onClick = {
            dropDownMenuSelection = "option2"
            isDropDownMenuExpanded = false
          })
          DropdownMenuItem(text = { Text("option3") }, onClick = {
            dropDownMenuSelection = "option3"
            isDropDownMenuExpanded = false
          })
          DropdownMenuItem(text = { Text("option4") }, onClick = {
            dropDownMenuSelection = "option4"
            isDropDownMenuExpanded = false
          })
          DropdownMenuItem(text = { Text("option5") }, onClick = {
            dropDownMenuSelection = "option5"
            isDropDownMenuExpanded = false
          })
        }
      }
      Icon(modifier = Modifier
        .clickable { onSortScreenClose(true) }
        .size(50.dp),
        imageVector = Icons.Outlined.Settings,
        contentDescription = "filter",
        tint = Color.Black)
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(3.dp)) {
      items(items = patents) { patent ->
        PatentCard(
          modifier = Modifier
            .height(50.dp)
            .clickable(onClick = { onPatentChose(patent) }),
          patent = patent
        )
      }
    }
  }

  if (isSettings) SortScreen(
    onClose = { onSortScreenClose(false) },
    onSortOptionChange = { onSortOptionChange(it) },
    sortOption = sortOption
  )
}

@Composable
fun PatentResult(modifier: Modifier = Modifier, patent: Patent, onBack: () -> Unit) {
  Icon(modifier = Modifier
    .clickable { onBack() }
    .size(40.dp),
    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
    contentDescription = "back",
    tint = Color.Black)
  Column(modifier = modifier.verticalScroll(rememberScrollState())) { // TODO: СДЕЛАТЬ СТОЛБИКИ НОРМАЛЬНО
    Text(
      "Авторы: ${patent.snippet.inventor}"
    )
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text(text = "Номер заявки:")
          Text(text = patent.common.application.number)
        }
        Column {
          Text(text = "Опубликовано:")
          Text(text = patent.common.publicationDate)
        }
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text(text = "Дата подачи заявки:")
          Text(text = patent.common.application.filingDate)
        }
        Column {
          Text(text = "МПК:")
          Text(text = patent.snippet.classification.ipc)
        }
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text(text = "Приоритетные данные:")
          Text(text = "ЧТО_ТО")
        }
        Column {
          Text(text = "Патентообладатели:")
          Text(text = patent.snippet.patentee)
        }
      }
    }
    Column {
      Text(text = "Описание")
      Text(
        text = patent.snippet.description
      )
    }
  }
}

