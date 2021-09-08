drop table board;

/*mysql에서는 주석처리 /* */ */
CREATE TABLE BOARD(
	BOARD_NUM INT,/*글 번호*/
	BOARD_NAME VARCHAR(20) NOT NULL,/*글 작성자*/
	BOARD_PASS VARCHAR(15) NOT NULL,/*글 비밀번호*/
	BOARD_SUBJECT VARCHAR(50) NOT NULL,/*글 제목*/
	BOARD_CONTENT VARCHAR(2000) NOT NULL,/*글 내용*/
	BOARD_FILE VARCHAR(50) NOT NULL,/*첨부 파일*/
	/*-----------------------------------------------*/
	BOARD_RE_REF INT NOT NULL,/*관련글 번호*/
	BOARD_RE_LEV INT NOT NULL,/*답글 레벨*/
	BOARD_RE_SEQ INT NOT NULL,/*관련글 중 출력순서*/
	/*----------------------------------------------*/
	BOARD_READCOUNT INT DEFAULT 0,/*조회수*/	
	BOARD_DATE DATE,/*작성일*/
	/*---------------------------------------------*/
	PRIMARY KEY(BOARD_NUM)/*기본키*/
);

create table board(
board_num int not null primary key,
board_name varchar(20) not null,
board_pass varchar(15) not null,
board_subject varchar(50) not null,
board_content varchar(2000) not null,
board_file varchar(50) not null,
board_re_ref int not null,
board_re_lev int not null,
board_re_seq int not null,
board_readcount int not null,
board_date date
);

--board_re_ref : 같은 수는 같은 그룹을 의미(원글의 board_re_ref 숫자가 3이면 답변글 모드 board_re_ref 숫자가 3이 되어 원글과 답변글을 하나로 묶을 수 있는 값)
--board_re_lev : 얼마만큼 안쪽으로 들어가 글이 시작될 것인지 결정해주는 값, 답변레벨로 0이 아니면[답변글]이다..(0이면 원글)
--답변레벨이 0이 아니면,[답변글]이면 답변레벨*2하여 답변레벨이 하나 증가할 때마다 공백(&nbsp;)을  2번씩 더 출력하여 들여쓰기한 후 답변글이 촐력되도록 로직처리
--board_re_seq : 원글에서 답변글이 몇 번째 아래에 놓일 것인지 위치를 결정해 주는 값

select * from board;
select IFNULL(max(board_num),0) from board;

--글등록
insert into board values(?,?,?,?,?,?,?,?,?,?,now());






