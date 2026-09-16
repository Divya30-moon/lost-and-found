package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.ItemResponseDTO;
import com.jsp.lostAndFound.dto.ItemSearchRequestDTO;

public interface SearchService {

    List<ItemResponseDTO> searchItems(ItemSearchRequestDTO request);

}