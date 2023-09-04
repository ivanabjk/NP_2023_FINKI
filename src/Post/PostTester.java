package Post;

import java.util.*;

class Indent {
    public static String indent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }
}

class Comment {
    String username;
    String id;
    String content;
    Integer likes;
    List<Comment> replies;

    public Comment(String username, String id, String content) {
        this.username = username;
        this.id = id;
        this.content = content;
        replies = new ArrayList<>();
        likes = 0;
    }
    public void addComment(String commentId, Comment reply) {
        if (this.id.equals(commentId)) {
            this.replies.add(reply);
        } else {
            this.replies.forEach(r -> r.addComment(commentId, reply));
        }
    }

    void addLike(String commentId) {
        if (id.equals(commentId)) {
            likes++;
        } else {
            replies.forEach(r -> r.addLike(commentId));
        }
    }

    public Integer getTotalLikes() {
        return likes + replies.stream().mapToInt(Comment::getTotalLikes).sum();
    }

    public String toString(int indent) {
        //Comment: Time claim water hand career we drug. Purpose size recently reveal usually door majority magazine. As nice real director politics will.
        //        Written by: user2
        //        Likes: 10
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sComment: %s\n", Indent.indent(indent), content));
        sb.append(String.format("%sWritten by: %s\n", Indent.indent(indent), username));
        sb.append(String.format("%sLikes: %s\n", Indent.indent(indent), likes));

        replies.sort(Comparator.comparing(Comment::getTotalLikes).reversed());
        replies.forEach(r -> sb.append(r.toString(indent + 1)));

        return sb.toString();
    }

}

class Post {
    String nameOfPost;
    String content;
    List<Comment> comments;

    public Post(String username, String postContent) {
        nameOfPost = username;
        content = postContent;
        comments = new ArrayList<>();
    }

    void addComment(String username, String commentId, String content, String replyToId) {
        Comment comment = new Comment(username, commentId, content);
        if (replyToId == null) {
            comments.add(comment);
        } else {
            comments.forEach(c -> c.addComment(replyToId, comment));
        }
    }

    void likeComment(String commentId) {
        comments.forEach(c -> c.addLike(commentId));

    }

    void sort() {
        comments.sort(Comparator.comparing(Comment::getTotalLikes).reversed());
    }

    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Post: %s\n", content));
        sb.append(String.format("Written by: %s\n", nameOfPost));
        sb.append("Comments:\n");
        for (Comment comment : comments) {
            sb.append(comment.toString(2));
        }
        return sb.toString();
    }
}

public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}
