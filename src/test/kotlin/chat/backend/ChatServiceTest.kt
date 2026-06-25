package chat.backend

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChatServiceTest {

    private fun serviceWithConversation(): Pair<ChatService, Conversation> {
        val service = ChatService()
        val conversation = service.createConversation("room", listOf("Alice", "Bob"))
        return service to conversation
    }

    @Test
    fun `sends a message with text and image attachment`() {
        val (service, conversation) = serviceWithConversation()
        val image = byteArrayOf(1, 2, 3)

        val message = service.sendMessage(conversation.id, "Alice", "hello", image)

        assertTrue(message != null)
        assertTrue(message.hasImage())
        assertEquals("hello", message.text)
        assertContentEquals(image, message.imageData)
    }

    @Test
    fun `sends an image-only message with empty caption without crashing`() {
        // Regression guard: empty text previously threw StringIndexOutOfBoundsException.
        val (service, conversation) = serviceWithConversation()

        val message = service.sendMessage(conversation.id, "Alice", "", byteArrayOf(9))

        assertTrue(message != null)
        assertTrue(message.hasImage())
        assertEquals("", message.text)
    }

    @Test
    fun `sends a text-only message with no image`() {
        val (service, conversation) = serviceWithConversation()

        val message = service.sendMessage(conversation.id, "Bob", "hi")

        assertTrue(message != null)
        assertFalse(message.hasImage())
        assertEquals("hi", message.text)
    }

    @Test
    fun `truncates text longer than the maximum length`() {
        val (service, conversation) = serviceWithConversation()
        val longText = "x".repeat(10_050)

        val message = service.sendMessage(conversation.id, "Alice", longText)

        assertTrue(message != null)
        assertEquals(10_000, message.text.length)
    }

    @Test
    fun `returns null when the conversation does not exist`() {
        val service = ChatService()

        val message = service.sendMessage(999L, "Alice", "hello")

        assertNull(message)
    }

    @Test
    fun `persists messages and keeps the image attachment intact`() {
        val (service, conversation) = serviceWithConversation()
        val image = byteArrayOf(4, 5, 6)
        service.sendMessage(conversation.id, "Alice", "with image", image)
        service.sendMessage(conversation.id, "Bob", "plain")

        val messages = service.getMessages(conversation.id)

        assertEquals(2, messages.size)
        assertContentEquals(image, messages.first().imageData)
        assertFalse(messages[1].hasImage())
    }

    @Test
    fun `adds the sender as a participant when they are not already in the conversation`() {
        val (service, conversation) = serviceWithConversation()

        service.sendMessage(conversation.id, "Carol", "hello")

        val updated = service.getConversation(conversation.id)
        assertTrue(updated != null && updated.hasParticipant("Carol"))
    }
}
