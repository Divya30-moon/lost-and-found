package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.MatchResponseDTO;

public interface MatchingService {

    List<MatchResponseDTO> findMatches(Long lostItemId);

}