package com.projectdemo1.repository.search;

import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.QBoard;
import com.projectdemo1.domain.QComment;
import com.projectdemo1.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

        booleanBuilder.or(board.title.contains("11")); // title like ...

        booleanBuilder.or(board.content.contains("11")); // content like ....

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));


        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();


        return null;

    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable, LocalDate startDate, LocalDate lastDate) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        // 검색 조건과 키워드가 있다면 처리
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        // 게시글 번호가 0보다 큰 게시글만 조회
        query.where(board.bno.gt(0L));

        // 날짜 조건 추가
        if (startDate != null && lastDate != null) {
            query.where(board.createdAt.between(startDate.atStartOfDay(), lastDate.atTime(23, 59, 59)));
        }

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }


    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QComment comment = QComment.comment;
        JPQLQuery<Board> query = from(board);
        query.leftJoin(comment).on(comment.board.eq(board));

        query.groupBy(board);

        if((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                        case "c":
                            booleanBuilder.or(board.content.contains(keyword));
                            break;
                            case "w":
                                booleanBuilder.or(board.writer.contains(keyword));
                                break;
                }
            }//end for
            query.where(booleanBuilder);
        }

        //bno >0
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.
                bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.createdAt,
                        comment.count().as("Comment Count")
                ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
