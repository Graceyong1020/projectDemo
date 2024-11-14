package com.projectdemo1.board4.repository.search;

import com.projectdemo1.board4.domain.Cboard;
import com.projectdemo1.board4.domain.QCboard;
import com.projectdemo1.domain.Board;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;




public class CboardSearchImpl extends QuerydslRepositorySupport implements CboardSearch {
    public CboardSearchImpl() {
        super(Cboard.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QCboard cboard = QCboard.cboard;

        JPQLQuery<Cboard> query = from(cboard);

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

        booleanBuilder.or(cboard.title.contains("11")); // title like ...

        booleanBuilder.or(cboard.content.contains("11")); // content like ....

        query.where(booleanBuilder);
        query.where(cboard.cno.gt(0L));


        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Cboard> list = query.fetch();

        long count = query.fetchCount();


        return null;
    }

    @Override
    public Page<Cboard> searchAll(String[] type, String keyword, Pageable pageable) {
        QCboard cboard = QCboard.cboard;

        JPQLQuery<Cboard> query = this.from(cboard);

        if((type != null && type.length > 0) && (keyword != null)) {
            BooleanBuilder builder = new BooleanBuilder();
            for(String t : type) {
                switch (t) {
                    case "t":
                        builder.or(cboard.title.contains(keyword)); //title에 keyword가 포함된 데이터를 가져오는 쿼리
                        break;
                    case "c":
                        builder.or(cboard.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(cboard.cno.stringValue().contains(keyword));
                        break;
                }
            }
            query.where(builder);


        }
        query.where(cboard.cno.gt(0l));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Cboard> list = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(list, pageable, total);

    }
}
