package com.oracolo.findmycar.telegram.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class TelegramInlineKeyboard {
	public TelegramInlineKeyboard() {
	}

	public static <T> List<List<T>> createInlineKeyboard(int numberOfElementsPerRow, List<T> elements) {
		int size = elements.size();
		int rows = size / numberOfElementsPerRow + size % numberOfElementsPerRow;
		List<List<T>> inlineKeyboard = new ArrayList<>();
		IntStream.range(0, rows).forEach((i) -> inlineKeyboard.add(new ArrayList<>()));
		IntStream.range(0, elements.size()).forEach((i) -> {
			Optional<List<T>> subListOptional = inlineKeyboard.stream().filter((subList) -> subList.size() < numberOfElementsPerRow).findFirst();
			subListOptional.ifPresent((subList) -> subList.add(elements.get(i)));
		});
		return inlineKeyboard;
	}
}
