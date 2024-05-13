package org.iot.hotelitybackend.hotelservice.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.iot.hotelitybackend.common.vo.ResponseVO;
import org.iot.hotelitybackend.hotelservice.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel-service")
public class ReservationController {

	private final ReservationService reservationService;
	private final ModelMapper mapper;

	@Autowired
	public ReservationController(ReservationService reservationService, ModelMapper mapper) {
		this.reservationService = reservationService;
		this.mapper = mapper;
	}

	/* 월별 예약 리스트 전체 조회 */
	/* 해당 월에 예약된 전체 리스트(페이징 처리 x)를 프론트로 넘겨주면
	 * 프론트에서 해당 리스트를 받아 날짜를 기준으로 예약 건 수를 카운트 하여 캘린더에 출력 */
	@GetMapping("/reservations/{reservationCheckinDate}")
	public ResponseEntity<ResponseVO> selectReservationListByMonth
										(@PathVariable("reservationCheckinDate") LocalDateTime reservationCheckinDate) {

		int year = reservationCheckinDate.getYear();
		int month = reservationCheckinDate.getMonthValue();

		Map<String, Object> reservationInfo = reservationService.selectReservationListByMonth(year, month);

		ResponseVO response = ResponseVO.builder()
			.data(reservationInfo)
			.resultCode(HttpStatus.OK.value())
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}

	/* 일자별 예약 내역 리스트 조회 */
	/* 캘린더에서 특정 일자 선택 시 조회되는 리스트 */
	@GetMapping("reservations/{reservationCheckinDate}/day")
	public ResponseEntity<ResponseVO> selectReservationListByDay
										(@PathVariable("reservationCheckinDate") LocalDateTime reservationCheckDate) {
		Map<String, Object> dailyReservationInfo = reservationService.selectReservationListByDay(reservationCheckDate);

		int year = reservationCheckDate.getYear();
		int month = reservationCheckDate.getMonthValue();
		int day = reservationCheckDate.getDayOfMonth();

		ResponseVO response = ResponseVO.builder()
			.data(dailyReservationInfo)
			.resultCode(HttpStatus.OK.value())
			.message(year + "/" + month + "/" + day + " 예약 내역 조회")
			.build();

		return ResponseEntity.status(response.getResultCode()).body(response);
	}
}
