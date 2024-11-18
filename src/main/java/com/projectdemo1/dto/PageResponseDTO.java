package com.projectdemo1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;   // 현재 페이지 번호
    private int size;   // 한 페이지당 출력될 데이터 수
    private int total;  // 전체 데이터 수

    // 시작 페이지 번호
    private int start;
    // 끝 페이지 번호
    private int end;

    // 이전 페이지 존재 여부
    private boolean prev;
    // 다음 페이지 존재 여부
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {

        // 전체 데이터가 없는 경우 리턴
        if (total <= 0) {
            return;
        }

        this.page = pageRequestDTO.getPage();  // 현재 페이지 번호
        this.size = pageRequestDTO.getSize();  // 페이지당 데이터 수
        this.total = total;  // 전체 데이터 수
        this.dtoList = dtoList;

        // 마지막 페이지 번호 계산
        this.end = (int) (Math.ceil(this.page / 10.0)) * 10;
        this.start = this.end - 9;

        // 실제 데이터에 따른 전체 페이지 수 계산
        int last = (int) (Math.ceil((total / (double) size)));

        // 끝 페이지 번호가 실제 마지막 페이지보다 클 수 없도록 조정
        this.end = Math.min(end, last);

        // 이전 페이지 여부: 시작 페이지가 1보다 크면 true
        this.prev = this.start > 1;

        // 다음 페이지 여부: 현재 끝 페이지 * 페이지당 데이터 수가 전체 데이터 수보다 작으면 true
        this.next = this.total > this.end * this.size;

        // 디버깅용 로그 출력
        System.out.println("Page: " + this.page);
        System.out.println("Size: " + this.size);
        System.out.println("Total: " + this.total);
        System.out.println("Start: " + this.start);
        System.out.println("End: " + this.end);
        System.out.println("Last: " + last);
        System.out.println("Prev: " + this.prev);
        System.out.println("Next: " + this.next);
    }

    public PageResponseDTO(List<E> dtoList, int totalPages, long totalElements) {
        this.dtoList = dtoList;
        this.total = (int) totalElements;
        this.size = dtoList.size();
        this.page = 1;
        this.start = 1;
        this.end = totalPages;
        this.prev = false;
        this.next = totalPages > 1;
    }
}
